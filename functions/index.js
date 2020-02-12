const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.pushes = functions.firestore
	.document('notifications/{token}')
	.onCreate((snap, context) => {
		const document = snap.data();
		console.log('document is', document);

		var registrationToken = context.params.token;
		var message = {
			data: {
				title: document.notification_titulo,
				body: document.notification_pergunta_resposta,
			},
			token: registrationToken
		}

		admin.messaging().send(message)
			.then((response) => {
				console.log('Successfully sent message:', response);
			})
			.catch((error) => {
				console.log('Error sending message:', error);
			})

		admin.firestore()
			.collection("notifications")
			.doc(registrationToken)
			.delete();

		return Promise.resolve(0);
	});

