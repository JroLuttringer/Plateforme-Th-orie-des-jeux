#!/usr/bin/Rscript
options(scipen=999)
x <- read.table('./logs/LOG')
names(x) <- c("IMAGE", "SIZE", "NG", "GEO", "TEMPERATURE", "VERSION",  "RUN", "RAM", "COST", "RUNTIME", "FIXED_ANGLE", "SEED")

y <- subset(x, x$IMAGE == './DB/2NG/Im1_1.txt')

y <- subset(y, select=c("IMAGE","NG", "GEO", "TEMPERATURE", "SEED"))
#y <- format(y$TEMPERATURE, scientific = FALSE)
y$IMAGE <- sub("./DB.*NG/", "", y$IMAGE)
write.table(y, './logs/images/test.txt', append = FALSE, quote = FALSE, sep = "_", row.names = FALSE, col.names = FALSE)
