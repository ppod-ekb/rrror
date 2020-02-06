package ru.cbr.rrror.service.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.cbr.rrror.service.db.model.*;
import ru.cbr.rrror.service.db.repository.CalcRepository;
import ru.cbr.rrror.service.db.repository.GroupRepository;
import ru.cbr.rrror.service.db.repository.UserRepository;
import ru.cbr.rrror.service.db.repository.RoleRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Component
@AllArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    @Component
    @AllArgsConstructor
    static class CalcMainLoader {
        private final CalcRepository repository;

        @AllArgsConstructor
        static class Config {
            int startYear;
            int stopYear;
            int numberOfRepSubj;
        }

        private File getRepSubjDataFile(String location) {
            try {
                return ResourceUtils.getFile(location);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("can't get file " + location + ", cause: " + e.toString(),e);
            }
        }

        public List<Calc> generate(Config config) {
            CalcStatus calcCompletedStatus = new CalcStatus("Расчет ОР выполнен", "CALC_COMPLETED");
            CalendarGenerator periodGenerator = new CalendarGenerator(config.startYear,config.stopYear);
            RepSubjGenerator repSubjGenerator = new RepSubjGenerator(
                                                    new RepSubjCsvReader(getRepSubjDataFile("classpath:banks.csv")));

            List<RepSubj> repSubjs = repSubjGenerator.generate().subList(0, config.numberOfRepSubj);
            List<Calc> calcs = new ArrayList<>();
            for (RegulationCalendar calendar : periodGenerator.generate()) {
                for (RepSubj repSubj : repSubjs) {
                    calcs.add(
                            new Calc(repSubj, calendar, calcCompletedStatus, true,
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate(),
                                    new CalcValueGenerator(100000000,999999999).generate()
                            )
                    );
                }
            }
            return calcs;
        };

        @AllArgsConstructor
        static class RepSubjCsvReader {
            private final File file;

            public List<RepSubjCsv> read() {
                List<RepSubjCsv> results = new ArrayList<>();
                try {
                    List<String> lines = Files.readAllLines(Paths.get(file.toURI()));
                    for (String line: lines) {
                        String[] split = line.split(";");
                        log.debug("line: " + line + ", split size: " + split.length);
                        new RepSubjCsvLineReader(split).get().ifPresent((r) -> results.add(r));
                    }

                    return results;
                } catch (IOException e) {
                    throw new RuntimeException("can't read file " + file.getAbsolutePath() + ", cause: " + e.toString(),e);
                }
            }

            @AllArgsConstructor
            static class RepSubjCsvLineReader {
                private final String[] line;

                public Optional<RepSubjCsv> get() {
                    Map<Integer, Supplier<RepSubjCsv>> factory = new HashMap<>();
                    factory.put(7, () -> new RepSubjCsv(line[0], "КО", line[1] + " " + line[2], line[3], line[4], line[5]+ " " +line[6]));
                    factory.put(6, () -> new RepSubjCsv(line[0], "КО", line[1] + " " + line[2], line[3], line[4], line[5]));
                    factory.put(8, () -> new RepSubjCsv(line[0], line[1], line[2] + " " + line[3], line[4], line[5], line[6]+ " " +line[7]));

                    if (factory.containsKey(line.length)) {
                        return Optional.of(factory.get(line.length).get());
                    } else {
                        return Optional.empty();
                    }
                }
            }
        }

        @AllArgsConstructor
        @Getter
        @ToString
        static class RepSubjCsv {
            private String id;
            private String type;
            private String name;
            private String regNum;
            private String licDateStr;
            private String address;
        }

        @AllArgsConstructor
        static class RepSubjGenerator {

            private final RepSubjCsvReader repSubjCsvReader;
            private final Function<RepSubjCsv, RepSubj> toRepSubj = (r) ->
                    new RepSubj(r.regNum, r.name, r.type, new LicTypeGenerator().generate(), new InnGenerator().generate());

            public List<RepSubj> generate() {
                List<RepSubj> repSubjs = new ArrayList<>();
                repSubjCsvReader.read().forEach(r1 -> {
                    log.debug(">>> add to repSubj: " + r1.toString());
                    toRepSubj.andThen(repSubjs::add).apply(r1);
                 });
                log.debug(">>> size of repSujs" + repSubjs.size());
                return repSubjs;
            }

            static class LicTypeGenerator {
                String[] types = new String[] {"Базовая", "Универсальная"};
                private static final Random random = new Random();

                public String generate() {
                    return types[random.nextInt(types.length)];
                }
            }

            static class InnGenerator {
                int size = 11;
                int[] numbers = new int[] {0,1,2,3,4,5,6,7,8,9};
                private static final Random random = new Random();

                public String generate() {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i <= size; i++) {
                        sb.append(numbers[random.nextInt(numbers.length)]);
                    }
                    return sb.toString();
                }
            }
        }

        @AllArgsConstructor
        static class CalendarGenerator {
            private final int startYear;
            private final int stopYear;

            private final Random random = new Random();

            public List<RegulationCalendar> generate() {
                List<RegulationCalendar> periods = new ArrayList<>();
                for (int year = startYear; year <= stopYear; year++) {
                    int month = random.nextInt(12)+1;
                    periods.add(new RegularPeriodGenerator(year, month).generate());
                }
                return periods;
            }

            @AllArgsConstructor
            static class RegularPeriodGenerator {
                private final int year;
                private final int month;

                private final Random random = new Random();


                public RegulationCalendar generate() {
                    int prevMonth = month == 1 ? month : month -1;

                    int regBeginDay = random.nextInt(27)+1;
                    int regEndDay = regBeginDay + random.nextInt(10)+1;
                    regEndDay =  regEndDay > 28 ? 28 : regEndDay;

                    LocalDate regBeginDt = LocalDate.of(year,prevMonth,regBeginDay);
                    LocalDate regEndDt = LocalDate.of(year,prevMonth,regEndDay);

                    int avgBeginDay = random.nextInt(27)+1;
                    int avgEndDay = avgBeginDay + random.nextInt(10)+1;
                    avgEndDay = avgEndDay > 28 ? 28 : avgEndDay;

                    LocalDate avgBeginDt = LocalDate.of(year,prevMonth,avgBeginDay);
                    LocalDate avgEndDt = LocalDate.of(year,prevMonth,avgEndDay);

                    return new RegulationCalendar(year, month,
                            regBeginDt,
                            regEndDt,
                            "Регулирование - с "+regBeginDt.toString()+" по " + regEndDt.toString(),
                            avgBeginDt,
                            avgEndDt,
                            "Усреднение - с "+avgBeginDt.toString()+" по " + avgEndDt.toString()
                    );
                }
            }
        }

        @AllArgsConstructor
        static class CalcValueGenerator {
            private final int min;
            private final int max;
            private final Random random = new Random();

            public BigDecimal generate() {
                return BigDecimal.valueOf((random.nextInt(((max-min)*10+1))+min*10) / 10.0);
            }
        }

        public void load() {
            List<Calc> calcs = generate(new Config(2015,2020, 10));

            for (Calc calc : calcs) {
                log.debug(">>> " + calc.toString());
            }

            repository.saveAll(calcs);
        }
    }

    @Component
    @AllArgsConstructor
    static class TestDbLoader {

        private final UserRepository repository;
        private final RoleRepository roleRepository;
        private final GroupRepository groupRepository;

        public void load() {
            log.debug(">>> run database loader");
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken("ppod-ekb", "doesn't matter",
                            AuthorityUtils.createAuthorityList("ROLE_AIB", "ROLE_USER")));

            Role roleUser = new Role("ROLE_USER");
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleAib = new Role("ROLE_AIB");

            Group all = new Group("ALL");

            User frodo = new User("frodo", new Name("Frodo", "Baggins"), "ring bearer").withRole(roleUser).withGroups(all);
            User bilbo = new User("bilbo", new Name("Bilbo", "Baggins"), "burglar").withRole(roleUser).withGroups(all);
            User gandalf = new User("gandalf", new Name("Gandalf", "the Grey"), "wizard").withRole(roleUser).withGroups(all);
            User sam = new User("samwise", new Name ("Samwise", "Gamgee"), "gardener").withRole(roleUser).withGroup(all);
            User meri = new User("meriadoc", new Name("Meriadoc", "Brandybuck"), "pony rider").withRole(roleAdmin).withGroup(all);
            User pere = new User("peregrin", new Name("Peregrin", "Took"), "pipe smoker").withRole(roleAdmin).withGroup(all);
            User ppod = new User("ppod-ekb", new Name("Pppod-ekb", "Pppod-ekb"), "Pppod-ekb developer").withRole(roleAib).withGroup(all);

            //all.withUsers(frodo,bilbo,gandalf,sam,meri,pere,ppod);

            roleUser.withUsers(frodo, bilbo, gandalf, sam);
            roleAdmin.withUsers(meri, pere);
            roleAib.withUsers(ppod);


        /*
        roleUser.getUsers().addAll(Arrays.asList(new User("frodo", "Frodo", "Baggins", "ring bearer").withRole(roleUser),
                new User("bilbo", "Bilbo", "Baggins", "burglar").withRole(roleUser),
                new User("gandalf", "Gandalf", "the Grey", "wizard").withRole(roleUser),
                (new User("samwise", "Samwise", "Gamgee", "gardener").withRole(roleUser)
                )));

        roleAdmin.getUsers().addAll(Arrays.asList(new User("meriadoc", "Meriadoc", "Brandybuck", "pony rider").withRole(roleAdmin),
                new User("peregrin", "Peregrin", "Took", "pipe smoker").withRole(roleAdmin)));

        roleAib.getUsers().addAll(Arrays.asList(new User("ppod-ekb", "Pppod-ekb", "Pppod-ekb", "Pppod-ekb developer").withRole(roleAib)));
        */

            //repository.saveAll(Arrays.asList(frodo, bilbo, gandalf, sam, meri, pere, ppod));

            roleRepository.saveAll(Arrays.asList(roleUser, roleAdmin, roleAib));

            groupRepository.save(new Group("NOT_ASSIGNED"));
            //userRoleRepository.save(roleAdmin);
            //userRoleRepository.save(roleAib);



        /*
        this.repository.save(new User("frodo", "Frodo", "Baggins", "ring bearer").withRole(roleUser));
        this.repository.save(new User("bilbo", "Bilbo", "Baggins", "burglar").withRole(roleUser));
        this.repository.save(new User("gandalf", "Gandalf", "the Grey", "wizard").withRole(roleUser));
        this.repository.save(new User("samwise", "Samwise", "Gamgee", "gardener").withRole(roleUser));
        this.repository.save(new User("meriadoc", "Meriadoc", "Brandybuck", "pony rider").withRole(roleAdmin));
        this.repository.save(new User("peregrin", "Peregrin", "Took", "pipe smoker").withRole(roleAdmin));
        this.repository.save(new User("ppod-ekb", "Pppod-ekb", "Pppod-ekb", "Pppod-ekb developer").withRole(roleAib));
        */
            log.debug("created " + this.repository.count() + " users");
        }
    }

    private final CalcMainLoader calcMainLoader;

    @Override
    public void run(String... strings) throws Exception {
        calcMainLoader.load();
    }
}

