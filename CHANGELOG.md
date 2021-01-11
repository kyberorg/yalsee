### Version 2.8 (07/01/2021)
Fresh UI

* More clean UI (#228)
* Fancy loading indicator (#271)
* Removed dead link old.yals.eu (#285)
* Fixed Mobile UI issues (#235)

### Version 2.7.6 (04/01/2021)
Version Bump

* Vaadin 14.4.5 (#259)
* Spring Boot 2.4.1 (#259)
* Telegram Bots 5.0.1 (#259) + code adaptation (#270)
* Small deps update (#259)
* JUnit 4 replaced with JUnit 5 (#266)
* Code: warnings fixed and deprecations and unused code removed (#280)
* JUnit 5 test report problem fixed (#278)

### Version 2.7.5 (30/12/2020)
No more leaks

* Application memory leak found and fixed (#264)
* OpenJ9 Java VM tweaks (#264)
* Migration from AppView (App Layout add-on) to Vaadin App layout (#269)
* Site title per each profile (#268)
* Small ops changes (#262)

### Version 2.7.4 (27/11/2020)
Internal Tweaks

* Memory usage decreased by switching to OpenJ9 (#216)
* Got rid of Vaadin Paid Components (#217)
* Dockerfile: JDK for Dev builds, JRE for others (#244)
* Dependencies updates (#238)
* Fixing application tests by moving to Selenide + Selenoid (#224) (#249)
* Added Java Remote Debug for DEV/DEMO (#213)
* Now deploying to K8S with Chuck Norris help (#218)
* Added APM agent for JVM stats (#223) (#242)
* Jenkinsfile updates  (#237) (#240) (#243)
* Logging: Removed extensive logging on debug level (#220)
* Fixed NPE at `ErrorUtils.java` (#212)
* Fixed Jenkins badges at `README.MD` (#246)
* Documented application.properties (#229)

### Version 2.7.3 (16/06/2020)
Super long links bug fixed

* Fixed a bug when super long URL with a lot params cannot be saved due to limitations (#207)

### Version 2.7.2 (08/06/2020)
Ops fixes

* Fixed illegal access warning during a startup in Java 11 + added some useful Java 11 params (#196)
* Fixed app restarts caused by DB connection aborted (#197)
* Startup banner replaced and now contains app name and version (#198)
* Fixing home-view.css loading error (#201)

### Version 2.7.1 (13/05/2020)
Bug fixes

* Bug fixed. EKSS Links can be used (#139)
* Adding missing image for 404 page (#184)
* Fixing IDN URL tests (#186)

### Version 2.7 (11/05/2020)
Vaadin UI

* User interface re-written in Vaadin 14 framework. (#55)

### Version 2.6 (30/09/19)
QR Code

* Added QR code with short link (#134)
* Added REST API Endpoint, which generates QR code from ident and optional size: `/api/qrCode/{ident}/{size}`

### Version 2.5.1 (25/09/19)
Updates and better error handling

* Software updates (#120)
* Replying to API calls only by JSON and respect Accept Header (#130)

### Version 2.5 (24/09/19)
Bug Fixes

* Links without http:// prefix are supported (#50)
* Links from Russian Wikipedia are supported (#92)
* Application moved to Spring Boot 2 (#101)
* IDN aka URLs with non-latin symbols are supported (#102)
* Application can correctly handle Database disconnects at runtime (#104)
* Removed double slash in git commit link (#105)
* Footer no more flaky (#106)
* Telegram auto config working stable (#108)
* Link counter shows without space after 1000 links saved (#122)

### Version 2.4 (12/04/18)
Telegram Bot

* Telegram Bot (#80)
* Better logs (#84)

### Version 2.3.1 (05/02/18)
Mattermost multiple params support and fixes

* Mattermost Bug: query with all spaces led to ":warning: Server Error" (#68)
* Multiple param support (#69)
* :warning: replaced with  :information_source: in Usage message (#70)

### Version 2.3 (31/01/18)
Mattermost integration

* Mattermost endpoint (#65)

### Version 2.2 (29/12/17)
Mobile-friendly site

* Tag footer no longer hides content on small screens (#34)
* Error box rewritten from static row to modal (#33)
* Internal: Move selenide selectors to separate class (#27)

### Version 2.1 (22/12/17)
Banners and copy to clipboard

* Banner about public access (#21)
* Banner: "N overall links saved" (#22)
* Copy to clipboard feature (#26)

### Version 2.0.2 (19/12/17)
Fix pack

* Docker ready Git-feature implementation (#1)
* Review technologies at humans.txt (#9)
* URL Args must be cleaned (#18)

### Version 2.0.1 (27/11/17)
Small fix

* Removed vertical scroll bar below (#14)

### Version 2.0 (11/07/17)
Second stable version

* Project was rewritten to _Spring Boot_ stack
* Makes short link from long one

### Version 1.0 and less (01/04/16)

Can be found from [here](https://github.com/kyberorg-archive/yals-play)