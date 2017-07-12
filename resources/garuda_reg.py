import pandas
import statsmodels.formula.api as smf
import statsmodels.api         as sm
import csv

#read features data
with open('resources/features.csv') as csvfile:
    x = list(csv.reader(csvfile))
features = [[float(j) for j in i] for i in x]
#print features

#read label data
with open('resources/label.csv') as csvfile:
    y = list(csv.reader(csvfile))
label_temp = [[float(j) for j in i] for i in y]
label = [val for sublist in label_temp for val in sublist]
#print label

#column naming
numparts = len(features[0])
partnames  = ['p%d'%p for p in range(numparts)]

#read partnames --somehow did not work
#with open('part.csv') as csvfile:
#    z = list(csv.reader(csvfile))
#partnames = [val for sublist in z for val in sublist]
#print len(partnames)

#ols regression
df = pandas.DataFrame(features,columns=partnames)
df['growth_rate'] = pandas.Series(label, index=df.index)

model_txt = 'growth_rate ~ ' + ' + '.join(partnames)
res = smf.ols(formula=model_txt, data=df).fit()
pred_toxic_parts = [x for x in res.pvalues.keys() if res.pvalues[x] < 0.001 and x != 'Intercept']
pred_toxic_probs = [res.pvalues[x] for x in pred_toxic_parts]

#print res.summary()
print ",".join(str(s) for s in pred_toxic_parts)
print ",".join(str(t) for t in pred_toxic_probs)

