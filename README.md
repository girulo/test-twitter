# README #

Please read the following instructions carefully and make sure that you fulfil all the requirements listed.

## Task ##

We would like you to write code that will cover the functionality listed below and provide us with the source as well as the output of an execution:

+ Connect to the [Twitter Streaming API](https://dev.twitter.com/streaming/overview)
	* Use the following values:
		+ Consumer Key: `vp8qXAMoZzy6jowJdtouPLUUb`
		+ Consumer Secret: `IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7`
	* The app name will be `interview-test`
	* You will need to login with Twitter
+ Filter messages that track on "bieber"
+ Retrieve the incoming messages for 30 seconds or up to 100 messages, whichever comes first
+ For each message, we will need the following:
	* The message ID
	* The creation date of the message as epoch value
	* The text of the message
	* The author of the message
+ For each author, we will need the following:
	* The user ID
	* The creation date of the user as epoch value
	* The name of the user
	* The screen name of the user
+ Your application should return the messages grouped by user (users sorted chronologically, ascending)
+ The messages per user should also be sorted chronologically, ascending
+ Print this information to the command line in a way that you consider suitable

## Provided functionality ##

The archive in itself is a [Maven project](http://maven.apache.org/) that contains functionality that will provide you with a `com.google.api.client.http.HttpRequestFactory` that is authorised to execute calls to the Twitter API in the scope of a specific user.
You will need to provide your _Consumer Key_ and _Consumer Secret_ and follow through the OAuth process (get temporary token, retrieve access URL, authorise application, enter PIN for authenticated token).
With the resulting factory you are able to generate and execute all necessary requests.
If you want to, you can also forego the provided classes and create your own but **do not use premade libraries** to interact with Twitter.
