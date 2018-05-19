# EdMon
EdMon helps you monitoring your server and services with real-time notifications. Just launch it from the command-line or with the included bash script, and you are good to go. Monitoring is based on ICMP and TCP.

## Features
- Monitors hosts with ICMP (ping)
- Monitors services with TCP (HTTP, SMTP, etc...)
- Notification sending by calling an external URL
- May run as master or secondary. In master mode, everything works as described here. In secondary mode (aka backup monitoring), notifications are only send if the master server is not available. Useful if you want EdMon to run on several hosts without being spammed by a lot of notifications. Only one server sends you a notification at a time.

## Installation
1) Make sure that Java 8 is installed
2) Download the latest archive is the Releases tab of GitHub, and extract it anywhere you want
3) Rename config.example.json to config.json and fill it with what you want (more explications below)
4) Setup a crontab or a scheduled task in order to make EdMon monitor your servers and services and warn you anytime there is an issue !

## Documentation
`config.json` is the only configuration file you need to edit in order to make EdMon suits your needs.

### General settings
The `servername` setting is required and should be filled with the name of the host where EdMon will run. It will be 

The `timeout` setting is required and specify in seconds the timeout for ICMP (ping) monitoring. Default setting (1) can be used, but you may need to change this if you are getting false-positive.

### ICMP monitoring
The `ping` section covers the hosts that will be monitored by a simple ICMP echo request (ping). You may add as many hosts as you want, following the scheme provided in the example config file.

### Services monitoring
The `services` section covers the services that will be monitored by trying to connect to TCP port (80 for HTTP for instance). You need to provide a name, a hostname (or IPv4/IPv6 address) and a port for each service you want to monitor.

### Schedule the monitoring task
EdMon monitors your hosts/services only when it is launched. If you want to use it as a monitoring server without the need of starting it manually, you have to setup a crontab or a scheduled task. A crontab can be done with `crontab -e` on UNIX, so that EdMon will run every minutes :

`* * * * * /path/of/your/installation/start_EdMon.sh &>/dev/null`


### Notification sending
EdMon supports sending notification when a host/services goes down and when it becomes available afterwards. This is handled by calling an URL. If you do not want to use this feature and just get command-line reports, you may leave the configuration as it is.

The `notifyURL` node in the configuration is named `EXAMPLE_notifyURL` in the example config in order to disable it. You need to rename it the `notifyURL` if you want to use the feature. It has to be configured with an HTTPS (all the web should be HTTPS now) address in which you can directly input the message that will be send (GET request). URL should end with and empty message field.

The french mobile carrier Free Mobile provides its customers with an API, allowing to send SMS to their phones by calling an URL. Thoses URLs are formatted this way :

`https://smsapi.free-mobile.fr/sendmsg?user=XXXXXXX&pass=XXXXXX&msg=This is the message here`

To use this URL with EdMon, you should specify it this way in the configuration :

`"notifyURL" : "https://smsapi.free-mobile.fr/sendmsg?user=XXXXXXX&pass=XXXXXX&msg=",`

so that EdMon will append the notification message to it and send you a notification.

This works with every GET API if you follow the scheme.

### Setup as secondary
EdMon can be run either as a master server or as a secondary server. When your main monitoring server is set up with EdMon, you can install another instance on another server. The problem is that when one of your monitored services goes down, you will receive two notifications, which is an issue.

By setting up your other installation as secondary, it will send you a notification only if the master server is not available (not responding to ping). In order to make this works, your master installation needs to respond to ping request. You also need to specify the master server hostname/IP in the secondary installation's configuration :

`"master": "your.master.installation.net"`

## Contributing
You are welcome to contribute to this project. It is build using maven, and I will always respond to issues or pull requests.