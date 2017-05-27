#!/bin/bash
PARTIE=$1
cat ./src/Client/Logs/$1/JOUEUR* > ./src/Client/Logs/$1/player_logs
cat ./src/Client/Logs/$1/IA* >> ./src/Client/Logs/$1/player_logs
cat ./src/Client/Logs/$1/PROD* > ./src/Client/Logs/$1/prod_logs
./R_SCRIPTS/joueur.r $1
