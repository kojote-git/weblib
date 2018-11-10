mvn clean package &&
sudo mv target/weblib.war /opt/tomcat/webapps &&
sudo systemctl restart tomcat
