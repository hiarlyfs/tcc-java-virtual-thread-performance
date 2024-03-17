import matplotlib.pyplot as plt
import numpy as np

# Dados fornecidos - Create Thread      
data_str1 = '''1,093
1,091
1,096
1,095
1,089
1,089
1,093
1,099
1,099
1,097
1,1
1,103
1,106
1,093
1,097
1,091
1,089
1,13
1,102
1,1
1,101
1,104
1,095
1,105
1,107
1,105
1,099
1,105
1,102
1,108'''

# Convertendo os dados para uma lista de números
data1 = [float(entry.replace(',', '.')) for entry in data_str1.split()]

# Ordenando os dados para calcular a ECDF
data_sorted1 = np.sort(data1)
n = len(data_sorted1)
ecdf_values = np.arange(1, n + 1) / n


data_str2 = '''0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002
0,002'''
# Convertendo os dados para uma lista de números
data2 = [float(entry.replace(',', '.')) for entry in data_str2.split()]

# Ordenando os dados para calcular a ECDF
data_sorted2 = np.sort(data2)
n2 = len(data_sorted2)
ecdf_values2 = np.arange(1, n2 + 1) / n2


# Plotando a ECDF
plt.figure(figsize=(20, 12))
plt.step(data_sorted1, ecdf_values, label='Start Threads')
plt.step(data_sorted2, ecdf_values2, label='Start Virtual Threads')
plt.xlabel('Tempo de execução (s)')
plt.ylabel('Probabilidade acumulada')
plt.title('ECDF')
plt.legend()
plt.grid(True)
plt.show()