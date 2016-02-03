# Sms2013-test.txt originally have 7 field, this small script combine the last two field which are the content of the message
infile = open("Sms2014-test.txt")
outfile = open("out.txt", "r+")
for line in infile:
	l = line.split('\t')
	if (len(l) > 6):
		l[5] = l[5] + l[6]
		del(l[6])
	outfile.write('\t'.join(l))
