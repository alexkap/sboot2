# sboot2
# MC challenge
A simple Spring Boot application to check if a route between two cities exists.
Requires a plain text file named "city.txt" located under config directory as a source of "map" data.
#### city.txt contents example :
<pre>Boston, New York
Philadelphia, Newark
Newark, Boston
Trenton, Albany</pre>

---
The application can be tested locally :

<pre>http://localhost:8080/connected?origin=Boston&destination=Newark</pre>

If the "route" can be built it will display "yes", otherwise "no"

You can also get a list of available cities :

<pre>http://localhost:8080/allcities</pre>

The project could  be built using simply mvn clean install.

Starting the embedded server / application (by default uses port 8080, can be changed in application.yml):

<pre>>>java -jar challenge-0.0.1-SNAPSHOT.jar</pre>
