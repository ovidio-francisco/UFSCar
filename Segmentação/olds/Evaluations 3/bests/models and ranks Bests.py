import Orange
import matplotlib.pyplot as plt

filetype = ".png"


print ("Vaaaaaaii!")

def plot(names, avranks, filname):
    arquivo = filename
    arquivo += filetype

    msg = "Gerando diagrama de diferenca critica em : "
    msg += arquivo

    print(msg)

    print (len(names))
    print (len(avranks))

    cd = Orange.evaluation.compute_CD(avranks, 12)
    Orange.evaluation.graph_ranks(avranks, names, cd=cd, filename=arquivo)
#    plt.show()

    print ("Ok")

    return


# Acuracy
filename = "Acuracy-2"
names   = ["C-40-11-t", "C-40-9-t", "TT-50-9", "TT-60-12"]
avranks = [2.0833, 2.25, 2.9583, 2.7083]
plot(names, avranks, filename)


# F1
filename = "F1-2"
names   = ["C-60-9-t", "C-80-9-t", "TT-50-3", "TT-40-3"]
avranks = [2.25, 2.625, 2.5833, 2.5417]
plot(names, avranks, filename)


# Precision
filename = "Precision-2"
names   = ["C-40-11-t", "C-40-9-t", "TT-60-12", "TT-50-9"]
avranks = [2, 2.1667, 2.6667, 3.1667]
plot(names, avranks, filename)


# Recall
filename = "Recall-2"
names   = ["C-100-9-f", "C-100-9-t", "TT-50-3", "TT-20-3"]
avranks = [2, 2, 2.875, 3.125]
plot(names, avranks, filename)


# WinDiff
filename = "WinDiff-2"
names   = ["C-40-11-t", "C-40-9-t", "TT-50-9", "TT-60-12"]
avranks = [2.0833, 2.25, 2.9583, 2.7083]
plot(names, avranks, filename)


# Pk
filename = "Pk-2"
names   = ["C-40-11-t", "C-40-9-t", "TT-30-9", "TT-30-12"]
avranks = [2.125, 2.4167, 2.625, 2.8333]
plot(names, avranks, filename)

 



