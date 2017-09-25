if [ ! -d "$2" ]; then
  echo "$2"
  mkdir $2
  echo "Directory created!"
fi
echo "Copy build library to the current workspace $1"
cp -rf $1/config/*.properties $2/
cp -rf $1/*.xml $2/
cp -rf $1/build_lib $2/
cp -rf $1/scripts $2/
chmod 777 -R $2
