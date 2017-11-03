import matplotlib.pyplot as plt
import numpy as np
import sys

print(sys.argv[4].split(','))
print(sys.argv[5].split(','))
print(sys.argv[6].split(','))

x = range(10)

#xlabel = ['DC-06', 'NMT-13', 'AAF-20', 'RHa-18', 'RHa-36', 'RHa-51', 'NMT-17', 'AcT-11', 'AcT-05', 'AAF-27', 'NMT-05', 'DC-02', 'AcT-09', 'DC-01', 'AAF-18']
xlabel = sys.argv[1].split(',')

y = sys.argv[2].split(',')
y1 = [float(t) for t in y]
#y1 = [0.08114336643748409, 0.02808441558441558, 0.0015342500636618284, 0.014085179526356004, 0.026687038451744333, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]

y = sys.argv[3].split(',')
y2 = [float(t) for t in y]
#y2 = [0.0, 0.0, 0.0, 0.0, 0.0, -0.0015851795263559926, -3.183091418390571E-6, -0.003211739241151007, -0.010466004583651642, -0.018417366946778715, -0.013442195059842121, -0.022975553857906797, -0.022975553857906797, -0.046412655971479505, -0.012044817927170867]

x_right = range(15)
xlabel_right = sys.argv[4].split(',')

y_pos = sys.argv[5].split(',')
y1_pos = [float(t) for t in y_pos]

y_neg = sys.argv[6].split(',')
y1_neg = [float(t) for t in y_neg]

y_ave = sys.argv[7].split(',')
y1_ave = [float(t) for t in y_ave]

y_err = sys.argv[8].split(',')
y1_err = [float(t) for t in y_err]

fig, ax = plt.subplots(2, 2, sharex='col')

ax[0, 0].plot(x, y1, color='c')
ax[1, 0].plot(x, y2, color='r')

ax[0, 0].set_title('Growth (predicted)')
ax[1, 0].set_title('Growth (real)')
ax[1, 0].set_xticks([i for i in range(0, len(xlabel))])
ax[1, 0].set_xticklabels(xlabel, rotation=90)
#ax[1, 0].axhline(y=sys.argv[9], linestyle='dashed', linewidth=0.5, color = 'k')


ax[0, 1].bar(x_right, y1_pos, color='c')
ax[0, 1].bar(x_right, y1_neg, color='r')

ax[1, 1].errorbar(x_right, y1_ave, y1_err, linestyle='None', marker='o')

ax[0, 1].set_title('Recommendation')
ax[1, 1].set_title('Average Growth')

ax[1, 0].set_xticks([i for i in range(0, len(xlabel))])
ax[1, 0].set_xticklabels(xlabel, rotation=90)

ax[1, 1].set_xticks([i for i in range(0, len(xlabel_right))])
ax[1, 1].set_xticklabels(xlabel_right, rotation=90)



#ax[0].set_xlabel('Part name',rotation=0)
#ax[0, 0].set_ylabel('Growth (predicted)',rotation=90)
#ax[1, 0].set_ylabel('Growth (real)',rotation=90)
#plt.legend(('Good', 'Bad'))

plt.savefig('resources/pythonplot/recommendation.png', bbox_inches='tight')

#plt.show()