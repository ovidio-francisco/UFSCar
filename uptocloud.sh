
source ~/.myfunctions/myfunctions.sh

checkOnline
if [ $online -ne 0 ] ; then
	echo ""
	echo "Where is the internet?"
	echo ""
	exit 4
fi


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

if [ $? -eq 0 ] ; then
	echo ""
	echo "commited"
	echo ""
else
	echo ""
	echo "commit failed!"
	echo ""
fi


git push -u origin master
echo ""


status=$(git status)

echo "status:"
$status | grep --color 'up-to-date\|nothing to commit'


if [[ "$status" == *"up-to-date"* ]] ; then 

	echo ""
	fortune | cowsay -f dragon
	echo ""
	echo "done!"
else

	cowsay -f duck "Something was wrong!" 
	echo ""
fi

