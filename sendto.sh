

if [ $# -lt 1 ] ; then
	echo ""
        echo "destination expected"
	echo ""
        exit 3
else
	echo ""
	echo "backing up!"
	echo ""
fi



path=`pwd`;
base=`basename "$path"`;
hoje=`date '+%d-%m-%y_%Hh%M'`;
filename=$1$base'_'$hoje'.tar.gz'

# echo $filename



    tput bold
    echo ""
    echo " Made some work?"
	echo ""
	echo " You are about to backup this folter to \"$filename\""
    echo ""
    echo ""
    tput sgr0

    read -p " Continue? [y/n] " yn
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



tar -czvf $filename --exclude='.git' *
#zip -r $filename .



echo ""

fortune | cowsay -f turtle

echo ""
echo "done!"








