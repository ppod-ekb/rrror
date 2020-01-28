'use strict';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const stompClient = require('./websocket-listener');
//const root = '/api'

// end::vars[]

// tag::app[]
class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {users: [], topic: []};
	}

	componentDidMount() {
        stompClient.register([
            {route: '/topic/createUser', callback: this.newEntityCreated},
            {route: '/topic/updateUser', callback: this.refreshCurrentPage},
            {route: '/topic/deleteUser', callback: this.refreshCurrentPage}

        ]);
    }

    entityCreated(message) {
        console.debug(">>> entity created: " + message);
    }
     entityUpdated(message) {

     }

     entityDeleted(message) {
     }


	render() {
		return (
			<div>RRROR client application</div>
		)
	}
}
// end::app[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)
// end::render[]

