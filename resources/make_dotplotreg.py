import matplotlib.pyplot as plt
import numpy as np

#full-8plates
x = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15])
y = np.array([0.217, 0.2294, 0.2005, 0.1599, 0.0054, 0.0115, 0.0205, 0.1046, 0.0764, -0.0177, -0.0274, -0.0502, 0.0146, 0.0101, -0.0009])
e = np.array([0.031, 0.084, 0.039, 0.038, 0.039, 0.03, 0.033, 0.039, 0.034, 0.029, 0.039, 0.038, 0.031, 0.057, 0.031])

xlabel = ['DC-06','AAF-20','NMT-13','NMT-17','AcT-05','Rha-18','Rha-51','NMT-05','AAF-27','Rha-36','AcT-11','AcT-09','DC-02','AAF-18','DC-01']

e2 = e * 2

#plt.figure(figsize=(10,8))
fig, ax = plt.subplots(figsize=(10, 8))

ax.errorbar(x, y, e2, linestyle='None', marker='o', color= 'r')

ax.set_title('Regression (8 plates)')
ax.set_xticks([i + 1 for i in range(0, len(xlabel))])
ax.set_xticklabels(xlabel, rotation=90)

plt.axhline(y=0.0, linestyle='dashed', linewidth=0.5, color = 'k')
plt.xlabel('Part name',rotation=0)
plt.ylabel('Coefficient',rotation=90)

plt.show()