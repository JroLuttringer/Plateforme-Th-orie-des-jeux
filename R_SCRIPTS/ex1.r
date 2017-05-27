#!/usr/bin/Rscript
options(width = "200", scipen=999)
x <- read.table('./logs/forcyril')
names(x) <- c("IMAGE", "SIZE", "NG", "GEO", "TEMPERATURE", "VERSION",  "RUN", "COST",  "RAM", "RUNTIME", "FIXED_ANGLE", "RATIO", "SEED")

# x <- subset(x, x$TEMPERATURE == 0)
# x$VERSION[x$RATIO==2 & x$VERSION == "0.3"] <- "0.2"

# x17 <- subset(x, x$SIZE == 17)
# x17 <- subset(x17, (x17$VERSION == "0.1") | (x17$VERSION == "0.2") | (x17$RATIO == 5))
# x33 <- subset(x, x$SIZE == 33)
# x33 <- subset(x33, (x33$VERSION == "0.1") | (x33$VERSION == "0.2") | (x33$RATIO == 5))
# x17
# x33

# x17_f <- subset(x17, x17$FIXED_ANGLE == 1)
# x17_uf <- subset(x17, x17$FIXED_ANGLE == 0)

# x33_f <- subset(x33, x33$FIXED_ANGLE == 1)
# x33_uf <- subset(x33, x33$FIXED_ANGLE == 0)

# x17_01 <- subset(x17, x17$VERSION == "0.1")
# x17_03 <- subset(x17, x17$VERSION == "0.3")
# x33_01 <- subset(x33, x33$VERSION == "0.1")
# x33_03 <- subset(x33, x33$VERSION == "0.3")

par(mfrow=c(1,2))
t1 = boxplot(x$RUNTIME~x$RATIO, main = "S17 RUNTIME/RATIO for unfixed angle")
# t2 = boxplot(x$RUNTIME~x$RATIO, main = "S17 RUNTIME/RATIO for unfixed angle")
# t3 = boxplot(x33_f$RUNTIME~x33_f$RATIO, main = "S33 RUNTIME/RATIO for fixed angle")
# t4 = boxplot(x33_uf$RUNTIME~x33_uf$RATIO, main = "S33 RUNTIME/RATIO for unfixed angle")


# par(mfrow=c(2,2))
t3 = boxplot(x$COST~x$RATIO, main = "S17 COST/RATIO for unfixed angle")
# t4 = boxplot(x$COST~x$RATIO, main = "S17 COST/RATIO for unfixed angle")
# t3 = boxplot(x33_f$COST~x33_f$VERSION, main = "S33 COST/VERSION for fixed angle")
# t4 = boxplot(x33_uf$COST~x33_uf$VERSION, main = "S33 COST/VERSION for unfixed angle")




# x11()






# fixed <- subset(table33, table33$FIXED_ANGLE == 1)
# unfixed <- subset(table33, table33$FIXED_ANGLE == 0)

# paste("cost mean fixed =", mean(fixed$COST))
# paste("cost mean unfixed =", mean(unfixed$COST))

# paste("time mean fixed =", mean(fixed$RUNTIME))
# paste("time mean unfixed =", mean(unfixed$RUNTIME))

# par(mfrow=c(2,2))

# f1 = boxplot(fixed$COST~fixed$RATIO, ylim=c(0.0, 0.3), main="COST/RATIO for FIXED ANGLE", xlab="1/x Change Angle/Change Voxel", ylab="Final Cost")
# f2 = boxplot(fixed$RUNTIME~fixed$RATIO, ylim=c(0,250), main="RUNTIME/RATIO for FIXED ANGLE", xlab="1/x Change Angle/Change Voxel", ylab="RUNTIME")
# u1 = boxplot(unfixed$COST~unfixed$RATIO, ylim=c(0.0, 0.12), main="COST/RATIO for UNFIXED ANGLE", xlab="1/x Change Angle/Change Voxel", ylab="Final Cost")
# u2 = boxplot(unfixed$RUNTIME~unfixed$RATIO, ylim=c(10,80), main="RUNTIME/RATIO for UNFIXED ANGLE", xlab="1/x Change Angle/Change Voxel", ylab="RUNTIME")
