#!/usr/bin/Rscript
options(width = "200", scipen=999)
args <- commandArgs(trailingOnly = TRUE)
test_var = (args[1])




#### PARTIE POUR TOUS LES PLAYERS #########
file_name = paste('./src/Client/Logs/',test_var,'/player_logs', sep="")
x <- read.table(file_name)
names(x) <- c("NAME","ACTION","SUCCESS","RESOURCE","QUANT","R1","R2","R3","TIME")


player_names <- unique(x$NAME)
number_player = length(player_names)
player_rows = 1
player_columns = 1
while (number_player > (player_rows*player_columns))
{
	if(player_columns > player_rows){
		player_rows = (player_rows + 1)
	}
	else{
		player_columns = (player_columns + 1)
	}
}

par(mfrow=c(player_columns, player_rows))
#For ALL NAME make x'name' subset
for (n in player_names)
{
	x1 <- subset(x, x$NAME == n)
	# x2 <- subset(x, x$NAME == 2)

	plot(x1$TIME, x1$R1, type="l", col="red", ylim=c(0,max(x$R1,x$R2,x$R3)), ylab="Resource quantities", main=paste("Resource over time of player",n))
	lines(x1$TIME, x1$R2, col="blue")
	lines(x1$TIME, x1$R3, col="green")
	legend(0, max(x$R1,x$R2,x$R3), c("fer","or","petrole"), cex=0.8, col=c("red","blue","green"),  lty=1);
}
########## FIN PARTIE PLAYERS ########


##### PARTIE POUR LES RESOURCES DES PRODS #########
file_name = paste('./src/Client/Logs/',test_var,'/prod_logs', sep="")
y <- read.table(file_name)
names(y) <- c("NAME","ACTION","SUCCESS","RESOURCE","QUANT","R1","R2","R3","TIME")
prod_names <- unique(y$NAME)
number_prods = length(prod_names)
prods_rows = 1
prods_columns = 1
while (number_prods > (prods_rows*prods_columns))
{
	if(prods_columns > prods_rows){
		prods_rows= (prods_rows + 1)
	}
	else{
		prods_columns = (prods_columns + 1)
	}
}


par(mfrow=c(prods_columns, prods_rows))
for (n in prod_names)
{
	y1 <- subset(y, y$NAME == n)
	#res_name = unique(y1$RESOURCE)

	plot(y1$TIME, y1$R1, type="l", col="red", ylim=c(0,max(y$R1,y$R2,y$R3)), ylab="Resource quantities", main=paste("Resource over time of ",n))
	lines(y1$TIME, y1$R2, col="blue")
	lines(y1$TIME, y1$R3, col="green")
	legend(0, max(y$R1,y$R2,y$R3), c("fer","or","petrole"), cex=0.8, col=c("red","blue","green"),  lty=1);

}
######## FIN PARTIE PRODS ########

#### PARTIE POUR LE % ########
par(mfrow=c(1,1))


file_name = paste('./src/Client/Logs/',test_var,'/Partie', sep="")
x3 <- read.table(file_name)
names(x3) <- c("NAME","R1","R2","R3","COMP")
# x3$R1 = x3$R1+x3$R2+x3$R3
#read file with player_names and total_res_objective and put into x3
player_number = 0
player_color = c("red", "blue", "green", "yellow", "purple", "black", "gray", "orange", "brown")
for (n in player_names)
{
	x1 <- subset(x, x$NAME == n)
	test_var = 0
	total1 = unique(subset(x3$R1, (x3$NAME == n)))
	x1$R1 = (x1$R1/total1*100)
	if (total1 == 0) x1$R1 = 0
	else test_var = test_var + 1
	x1$R1[x1$R1>100] = 100


	total2 = unique(subset(x3$R2, (x3$NAME == n)))
	x1$R2 = (x1$R2/total2*100)
	if (total2 == 0) x1$R2 = 0
	else test_var = test_var + 1
	x1$R2[x1$R2>100] = 100


	total3 = unique(subset(x3$R3, (x3$NAME == n)))
	x1$R3 = (x1$R3/total3*100)
	if (total3 == 0) x1$R3 = 0
	else test_var = test_var + 1
	x1$R3[x1$R3>100] = 100

	x1$R1 = (x1$R1 + x1$R2 + x1$R3) /test_var
	# total = ((total3 + total2 + total1) / 3)
	if (player_number == 0)
	{
		plot(x1$TIME, x1$R1, type="l", col="red", xlim=c(0,max(x$TIME)),ylim=c(0,100), ylab="Percentage of objective quantities", main=paste("Progress over time of all player"))
		player_number = 2
	}
	else 
	{
		lines(x1$TIME, x1$R1, col=player_color[player_number])
		player_number = player_number +1
	}
}
play_col <- player_color[c(1:player_number)]
legend(0, 100, player_names, cex=0.8, col=play_col,  lty=1);


#### FIN PARTIE POUR LE % ########
