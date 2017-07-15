

if [ $# -lt 1 ] ; then
	echo ""
        echo "message commit expected"
	echo ""
        exit 3
fi


    tput bold
    echo ""
    echo " Made some work?"
    echo ""
    tput sgr0

    read -p " Do you wish to update the remote repository with the local data? [y/n] " yn
    case $yn in
        [Nn]* ) echo "" ; 
			echo "Ok. We didn't nothing! Good bye.";
			echo "";
			exit ;;

        [Yy]* ) echo ""; 

			echo "";;

        * ) echo "Please answer yes or no.";
			exit ;;
    esac


echo ""
echo "backing up!"
echo "commit message --> \"$1\""
echo ""

git add .
git commit -m "$1"

echo ""
echo "commited"
echo ""


git push -u origin master

echo ""

if [ "$(git status)" == *"up-to-date"* ] ; then 

	fortune | cowsay -f dragon

	echo ""
	echo "done!"
else

	cowsay "Something was wrong!" -f turtle

	echo ""
fi







# if [ -z "$(git status --porcelain)" ] ; then 
