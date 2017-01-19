import pandas
import matplotlib
import matplotlib.pyplot as plt
import numpy as np
from random import choice

import statsmodels.formula.api as smf
import statsmodels.api         as sm


numdesigns = 20
numgenes   = 4
healthy_mean = 0.9
healthy_sd   = 0.3

genenames = list("ABCD")
geneidxs   = range(len(genenames))

fakedata = np.zeros(shape=(numdesigns,len(genenames)))

fakedata2 = [[ 1, 1, 1, 0 ],
 [ 1, 0, 1, 1 ],
 [ 1, 0, 1, 1 ],
 [ 1, 0, 1, 0 ],
 [ 1, 1, 1, 1 ],
 [ 1, 1, 1, 0 ],
 [ 1, 0, 1, 0 ],
 [ 1, 1, 1, 0 ],
 [ 1, 1, 0, 0 ],
 [ 1, 0, 1, 0 ],
 [ 0, 0, 1, 1 ],
 [ 1, 0, 1, 1 ],
 [ 1, 1, 0, 0 ],
 [ 0, 1, 1, 1 ],
 [ 0, 1, 1, 0 ],
 [ 1, 1, 0, 1 ],
 [ 0, 1, 1, 0 ],
 [ 1, 1, 0, 0 ],
 [ 1, 0, 1, 0 ],
 [ 1, 1, 1, 0 ]]

for i in range(numdesigns):
    genes = set()
    for j in range(numgenes):
        geneidx = choice(geneidxs)
        genes.add(geneidx)
    for geneidx in genes:
        fakedata[i,geneidx] = 1.0
        
print fakedata2

growth_rates = np.zeros(numdesigns)

for i in range(numdesigns):
    growth_rates[i] = np.random.normal(size=1,loc=healthy_mean,scale=healthy_sd)
    
growth_rates2 = [ 1.29059717,  0.70544841,  0.75630101,  0.97549496,  0.43153817,  0.44917647,
  0.99678689,  0.90981844,  0.80678932,  0.83838037,  1.34614472,  1.43276749,
  0.93183209,  0.89969879,  0.98147421,  0.66831309,  1.28875777,  0.958625,
  0.66133257,  0.71738615]

print growth_rates2

df = pandas.DataFrame(fakedata2,columns=genenames)
df['growth_rate'] = pandas.Series(growth_rates2, index=df.index)

print df

model_txt = 'growth_rate ~ ' + ' + '.join(genenames)
print model_txt
print 
res = smf.ols(formula=model_txt, data=df).fit()

print res.summary()

for gene in res.pvalues.keys():
    pvalue = res.pvalues[gene]
    if pvalue < 0.01:
        print gene, res.params[gene], res.pvalues[gene]