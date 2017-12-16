Taking as reference
the idea of (Ponte and Croft, 1997) who take into account
the preceding and the following contexts of a segment, we
calculate the informative similarity of each sentence in the
corpus with its surrounding pieces of texts i.e. its previous
block of k sentences and its next block of k sentences. The
idea is to know whether the focus sentence is more similar
to the preceding block of sentences or to the following
block of sentences.

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/Topic segmentation algorithms for text summarization and passage retrieval: An exhaustive evaluation.pdf





TopicTiling precisa de treinamento

Riedl and Biemann (2012), based on TextTiling, proposed the
TopicTiling algorithm that segments documents using the Latent Dirichlet
Allocation (LDA) topic model (Blei et al., 2003). The documents that are
to be segmented have first to be annotated with topic IDs, obtained by the
LDA inference method. The topic model must be trained on documents
similar in content to the test documents. The IDs are used to calculate the
cosine similarity between two adjacent sentence blocks, represented as two
vectors, containing the frequency of each topic ID. Values close to 0 indicate
marginal relatedness between two adjacent blocks, whereas values close to 1
denote connectivity. For evaluating, the authors applied WindowDiff measure
(Pevzner and Hearst, 2002): the results have shown that TopicTiling improves
the state of the art.

Subtopic annotation and automatic segmentation for news texts in Brazilian Portuguese.pdf
http://www.sfu.ca/~mtaboada/docs/publications/Cardoso_Pardo_Taboada_2017.pdf





Rather than using the raw term frequency vector, some
approaches, e.g., BAYESSEG[2], [3], TopicTiling[4], TSM[5],
NTSeg[6], employ topic modeling to improve performance,
such as latent Dirichlet allocation[7]

file:///ext4Data/UFSCar/papers/atalhos/Domain-independent unsupervised text segmentation for data management.pdf

