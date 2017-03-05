import orange, orngStat

names = ["MNB", "Self-Training (MNB)", "EM", "kNN", "SVM", "GNetMine", "LPBHN", "TagBased", "TCBHN"]

avranks =  [6.5638, 3.2188, 3.2812, 7.1875, 6.3125, 5.0312, 7.9062, 3.8438, 1.625]

cd = orngStat.compute_CD(avranks, 16, type="nemenyi", alpha="0.05")

orngStat.graph_ranks("aspecto-macro.png", avranks, names, cd=cd, width=10, textspace=2.2)

print cd ;
