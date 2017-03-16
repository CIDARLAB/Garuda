import pandas
import matplotlib
import matplotlib.pyplot as plt
import numpy as np
from random import choice

import statsmodels.formula.api as smf
import statsmodels.api         as sm


numdesigns = 300
numgenes   = 300
pathwaylen = 4
num_toxicgenes = 4

healthy_mean = 0.9
healthy_sd   = 0.3
sick_mean    = 0.4
sick_sd      = 0.4

genenames = ['gene%d'%x for x in range(numgenes)]
geneidxs   = range(numgenes)

fakedata = np.zeros(shape=(numdesigns,numgenes))

for i in range(numdesigns):
    genes = set()
    for j in range(pathwaylen):
        geneidx = choice(geneidxs)
        genes.add(geneidx)
    for geneidx in genes:
        fakedata[i,geneidx] = 1.0
    
toxic_genes = genenames[3:num_toxicgenes+3]
toxic_idxs  = [genenames.index(x) for x in toxic_genes]
growth_rates = np.zeros(numdesigns)

sick_designs = np.zeros(numdesigns,dtype=bool)

for i in range(numdesigns):
    sick_flag = False
    for toxic_idx in toxic_idxs:
        if fakedata[i,toxic_idx] != 0:
            sick_flag = True
    if sick_flag:
        growth_rates[i] = np.random.normal(size=1,loc=sick_mean,scale=sick_sd)[0]
        sick_designs[i] = True
    else:
        growth_rates[i] = np.random.normal(size=1,loc=healthy_mean,scale=healthy_sd)[0]

df = pandas.DataFrame(fakedata,columns=genenames)
df['growth_rate'] = pandas.Series(growth_rates, index=df.index)
df['sick_design'] = pandas.Series(sick_designs, index=df.index) 

#print df

model_txt = 'growth_rate ~ ' + ' + '.join(genenames)
print model_txt
print 
res = smf.ols(formula=model_txt, data=df).fit()

#print res.summary()

for gene in res.pvalues.keys():
    pvalue = res.pvalues[gene]
    if pvalue < 0.01:
        print gene, res.params[gene], res.pvalues[gene]