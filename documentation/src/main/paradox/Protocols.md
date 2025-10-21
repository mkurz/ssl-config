# Configuring Protocols

By default, WS SSL will use the most secure version of the TLS protocol
available in the JVM.  On JDK 8u341 and later, the default protocol is "TLSv1.3".
 
The full protocol list in JSSE is available in the [Standard Algorithm Name Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#jssenames).

## Defining the default protocol

If you want to define a different [default protocol](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLContext.html#getInstance-java.lang.String-),
you can set it specifically in the client:

```conf
# Passed into SSLContext.getInstance()
ssl-config.protocol = "TLSv1.3"
```

If you want to define the list of enabled protocols, you can do so
explicitly:

```conf
# passed into sslContext.getDefaultParameters().setEnabledProtocols()
ssl-config.enabledProtocols = [
  "TLSv1.3",
  "TLSv1.2"
]
```

If you are on JDK 1.8, you can also set the `jdk.tls.client.protocols`
system property to enable client protocols globally.

WS recognizes "SSLv3", "SSLv2" and "SSLv2Hello" as weak protocols with a
number of [security issues](https://www.schneier.com/paper-ssl.pdf),
and will throw an exception if they are in the
`ssl-config.enabledProtocols` list. Virtually all servers support
`TLSv1.2` or newer, so there is no advantage in using these older protocols.
