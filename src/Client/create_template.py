#!/usr/bin/python

import argparse
import os
import csv
import random

parser=argparse.ArgumentParser()
parser.add_argument("-p","--prod", type=int, default = 0)
parser.add_argument("-i","--IA", type=int, default=0)
parser.add_argument("-j","--joueur", type=int, default=0)
parser.add_argument("-c","--coord", type=int,default = 0)
parser.add_argument("-o","--out", type=str, default="conf_partie")
parser.add_argument("-f","--fileserv", type=str, default="serveurs.csv")


args = parser.parse_args()


if os.path.exists(args.out):
    os.remove(args.out)

prod_template = "TYPE=PROD\nNOM=PROD_{0}\nRESSOURCES=Or,100,Petrole,100,Fer,100\nINTERVALLE=2000\nADRESSE={1}\n\n"

IA_template = "TYPE=IA\nNOM=IA_{0}\nOBJECTIFS=Or,500,Petrole,500,Fer,500\nADRESSE={1}\nCOMPORTEMENT=Average\n\n"

joueur_template = "TYPE=JOUEUR\nNOM=JOUEUR_{0}\nOBJECTIFS=Or,500,Petrole,500,Fer,500\nADRESSE={1}\n\n"

coord_template = "TYPE=COORD\nNOM=Coord_{0}\nADRESSE={1}\n\n"

partie_template = "TYPE=PARTIE\nNOM=NOM_PARTIE\nMAXTAKE=25\nPUNVOL=30\nVICTOIRE=Premier\nTOURPTOUR=false\nNB_IA={0}\nNB_JOUEUR={1}\nNB_PROD={2}\nNB_COORD={3}\n\n".format(args.IA,args.joueur,args.prod,args.coord)

print "Creation d'une template de configuration pour : \n - {0} Joueur(s), {1} Producteur, {2} IAs".format(args.joueur, args.prod, args.IA)
nbserv = 0;
with open(args.fileserv) as f:
      nbserv = len(f.readlines())
print "Distribution des adresses aleatoirement sur les {0} serveur(s) disponible(s)".format(nbserv) 



with open(args.out,"w") as f:
    f.write(partie_template)
    for i in xrange(0,args.prod):
        f.write(prod_template.format(i,random.choice(open(args.fileserv).readlines()).replace(",","/").rstrip('\n')))

    for i in xrange(0,args.IA):
        f.write(IA_template.format(i,random.choice(open(args.fileserv).readlines()).replace(",","/").rstrip('\n')))

    for i in xrange(0, args.joueur):
        f.write(joueur_template.format(i, random.choice(open(args.fileserv).readlines()).replace(",","/").rstrip('\n')))

    for i in xrange(0, args.coord):
        f.write(coord_template.format(i,random.choice(open(args.fileserv).readlines()).replace(",","/").rstrip('\n')))
    


