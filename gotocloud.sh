if [ $# -lt 1 ] ; then
        echo "message commit expected"
        exit 3
fi


echo "backing up!"
git add .
git commit -m "$1"
git push -u origin master
fortune | cowsay

echo ""
echo "done!"
