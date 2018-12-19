#
# DB creation
#

- step 1: install postgresql on your server

- step 2: create a database, user and password

		sudo -u postgres psql
		postgres=# create database nagoyatdb1;
		postgres=# create user unagoyatest with encrypted password 'Nagoyat2018';
		postgres=# grant all privileges on database nagoyatdb1 to unagoyatest;