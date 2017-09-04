import pandas
import statsmodels.formula.api as smf
import statsmodels.api         as sm
import csv

#read features data
with open('features-simplified-del199.csv') as csvfile:
    x = list(csv.reader(csvfile))
features = [[float(j) for j in i] for i in x]
#print features

#read label data
with open('label-simplified-del199.csv') as csvfile:
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