# Auto-Reply-WebApp
    sudo apt-get install tomcat8
    # fix directories
    cd /usr/share/tomcat8
    sudo ln -s /var/lib/tomcat8/conf conf
    sudo ln -s /var/log/tomcat8 log
    sudo ln -s /etc/tomcat8/policy.d/03catalina.policy conf/catalina.policy
    sudo chmod -R a+rwx /usr/share/tomcat7/conf
    # allow startup from inside eclipse
    /etc/init.d/tomcat8 stop 