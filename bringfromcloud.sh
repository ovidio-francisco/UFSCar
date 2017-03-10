


    read -p "Do you wish to discard the local changes and get the latest commits in the remote repository? " yn
    case $yn in
        [Nn]* ) echo "" ; 
		echo "Ok. We didn't nothing! Good bye.";
		echo "";
		exit ;;

        [Yy]* ) echo ""; 

		# Discard the local changes
		echo "Discarding the local changes";
		git reset --hard

		echo "Getting the latest commits from the repository";
		git pull origin master

		echo "";;

        * ) echo "Please answer yes or no.";;
    esac


echo "Finish"


#http://stackoverflow.com/questions/15745045/how-do-i-resolve-git-saying-commit-your-changes-or-stash-them-before-you-can-me

