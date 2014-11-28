
#! /bin/bash

echo "Content-Type: text/plain"
echo ""

credential="`java Lookup -d "$QUERY_STRING"`"

i=`expr index $credential ':'`
password=${credential:$i}
username=${credential:0:$i-1}


login_command="`/eecs/home/cse03257/www/sshpass -p "$password" ssh "$username"@red.cs.yorku.ca e$
result=$?

if [ $result -eq 0 ]
then

    	echo "`java Lookup -n "$username"`"
else
    	echo no
fi

