#curl http://localhost:8000/api/v1/receiver/?access_key=5298fe82fb21ab1b8b725333&format=json

data=("one", "two", "three")

for i in "${data[@]}"
do	
	echo $i
done 