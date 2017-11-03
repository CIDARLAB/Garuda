import pandas
import statsmodels.formula.api as smf
import statsmodels.api         as sm
import csv
from math import log

#read features data
with open('features_sampled_regression.csv') as csvfile:
    x = list(csv.reader(csvfile))
features = [[float(j) for j in i] for i in x]
#print features

#read label data
with open('label_sampled_regression.csv') as csvfile:
    y = list(csv.reader(csvfile))
label_temp = [[float(j) for j in i] for i in y]
label = [val for sublist in label_temp for val in sublist]
#print label

#column naming
numparts = len(features[0])
partnames  = ['part%d'%p for p in range(numparts)]

#ols regression
df = pandas.DataFrame(features,columns=partnames)
df['growth_rate'] = pandas.Series(label, index=df.index)

model_txt = 'growth_rate ~ ' + ' + '.join(partnames)
print (model_txt)
res = smf.ols(formula=model_txt, data=df).fit()
print (res.summary())

pvalues_arr = []

with open('pvalues.csv', 'wb') as csvfile:
	for key in res.pvalues.keys():
		pval = round(res.pvalues[key], 3)
		if pval < 0.001:
			#csvfile.write(bytes (str(0.001), 'UTF-8'))
			#csvfile.write(bytes (",", 'UTF-8'))
			csvfile.write(bytes (str(log(0.001, 0.5)), 'UTF-8'))
		else:
			#csvfile.write(bytes (str(pval), 'UTF-8'))
			#csvfile.write(bytes (",", 'UTF-8'))
			csvfile.write(bytes (str(log(pval, 0.5)), 'UTF-8'))
		csvfile.write(bytes ("\n", 'UTF-8'))

params_arr = []

with open('params.csv', 'wb') as csvfile:
	for key in res.pvalues.keys():
		csvfile.write(bytes (str(res.params[key]), 'UTF-8'))
		csvfile.write(bytes ("\n", 'UTF-8'))