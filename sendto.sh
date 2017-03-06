

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
hoje=`date '+%d-%m-%y'`;
filename=$1$base'_'$hoje'.tar.gz'

echo "destination --> \"$filename\""
echo $filename

tar -czvf $filename .
#zip -r topendrive.zip .





