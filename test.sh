
status=$(git status)

cmd="echo $status \| grep --color 'up-to-date\|nothing to commit'"
echo $cmd

eval $cmd


