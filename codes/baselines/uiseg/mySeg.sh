if [ $2 -gt 0 ]; then
	cat $1 | ./prep-seg | ./seg -maxNumSegs $2 | tail -1 > vai.seg
	./seg-comb vai.seg $1

else
	cat $1 | ./prep-seg | ./vseg > vai.seg
	./seg-comb vai.seg $1

fi

rm vai.seg
