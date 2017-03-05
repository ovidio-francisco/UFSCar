

if [ $# -lt 1 ] ; then
	echo ""
        echo "message commit expected"
	echo ""
        exit 3
else
	echo ""
	echo "backing up!"
	echo "commit message --> \"$1\""
	echo ""
fi



git add .
git commit -m "$1"
git push -u origin master
fortune | cowsay -f dragon -W 80

echo ""
echo "done!"
