package org.interview.oauth.twitter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class TwitterAuthenticator {

	private final PrintStream out;
	
	private final String consumerKey;
	private final String consumerSecret;
	
	private HttpRequestFactory factory;

	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";

	public TwitterAuthenticator(final PrintStream out, final String consumerKey, final String consumerSecret) {
		this.out = out;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public synchronized HttpRequestFactory getAuthorizedHttpRequestFactory() throws TwitterAuthenticationException {
		if (factory != null) {
			return factory;
		}

		OAuthHmacSigner signer = new OAuthHmacSigner();

		signer.clientSharedSecret = consumerSecret;

		OAuthGetTemporaryToken requestToken = new OAuthGetTemporaryToken(REQUEST_TOKEN_URL);
		requestToken.consumerKey = consumerKey;
		requestToken.transport = TRANSPORT;
		requestToken.signer = signer;

		OAuthCredentialsResponse requestTokenResponse;
		try {
			requestTokenResponse= requestToken.execute();
		} catch (IOException e) {
			throw new TwitterAuthenticationException("Unable to aquire temporary token: " + e.getMessage(), e);
		}

		out.println("Aquired temporary token...\n");

		signer.tokenSharedSecret = requestTokenResponse.tokenSecret;

		OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(AUTHORIZE_URL);
		authorizeUrl.temporaryToken = requestTokenResponse.token;

		String providedPin;
		Scanner scanner = new Scanner(System.in);
		try {
			out.println("Go to the following link in your browser:\n" + authorizeUrl.build());
			out.println("\nPlease enter the retrieved PIN:");
			providedPin = scanner.nextLine();
		} finally {
			scanner.close();
		}

		if (providedPin == null) {
			throw new TwitterAuthenticationException("Unable to read entered PIN");
		}

		OAuthGetAccessToken accessToken = new OAuthGetAccessToken(ACCESS_TOKEN_URL);
		accessToken.verifier = providedPin;
		accessToken.consumerKey = consumerSecret;
		accessToken.signer = signer;
		accessToken.transport = TRANSPORT;
		accessToken.temporaryToken = requestTokenResponse.token;


		OAuthCredentialsResponse accessTokenResponse;
		try {
			accessTokenResponse= accessToken.execute();
		} catch (IOException e) {
			throw new TwitterAuthenticationException("Unable to authorize access: " + e.getMessage(), e);
		}
		out.println("\nAuthorization was successful");

		signer.tokenSharedSecret = accessTokenResponse.tokenSecret;

		OAuthParameters parameters = new OAuthParameters();
		parameters.consumerKey = consumerKey;
		parameters.token = accessTokenResponse.token;
		parameters.signer = signer;

		factory = TRANSPORT.createRequestFactory(parameters);
		return factory;
	}
}

