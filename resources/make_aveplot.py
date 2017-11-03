import matplotlib.pyplot as plt
import numpy as np

x = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15])
y = np.array([0.8898742457191009, 0.8373060811728393, 0.8270010542857144, 0.7933718751985296, 0.7909021139166668, 0.7908547448999994, 0.7843932331882353, 0.7614991344878049, 0.7541665062405064, 0.7425706994531253, 0.736668547375, 0.730159857588889, 0.7175463052888889, 0.6929035131954021, 0.6547255778235294])
e = np.array([0.166209225348191, 0.26105970279747126, 0.19414876717787466, 0.23468155486651257, 0.2865893966136742, 0.2762255385997427, 0.2204416007392759, 0.24235039422446128, 0.27258456939078113, 0.3016713492790439, 0.23366702922826169, 0.31347986000956196, 0.2887661047438191, 0.22858247404475282, 0.20548611032062802])
xlabel = ['DC-06', 'NMT-13', 'AAF-20', 'RHa-18', 'RHa-36', 'RHa-51', 'NMT-17', 'AcT-11', 'AcT-05', 'AAF-27', 'NMT-05', 'DC-02', 'AcT-09', 'DC-01', 'AAF-18']
#colors = ['red', 'red', 'red', 'red', 'black', 'black', 'black', 'red', 'black', 'black', 'black', 'black', 'black', 'black', 'black']


fig, ax = plt.subplots(figsize=(10, 8))

ax.errorbar(x, y, e, linestyle='None', marker='o')

ax.set_title('Average Growth (8 plates)')
ax.set_xticks([i + 1 for i in range(0, len(xlabel))])
ax.set_xticklabels(xlabel, rotation=90)

#for xtick, color in zip(ax.get_xticklabels(), colors):
#    xtick.set_color(color)


#plt.axhline(y=0.0, linestyle='dashed', linewidth=0.5, color = 'k')
plt.xlabel('Part name',rotation=0)
plt.ylabel('Growth',rotation=90)

plt.show()