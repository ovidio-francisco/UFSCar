
The TextTiling algorithm, introduced by Hearst (?), is a simple, domain-independent
technique that assigns a score to each topic boundary candidate (inter-sentence gap)
based on a cosine similarity measure between chunks of words appearing to the left
and right of the candidate. Topic boundaries are placed at the locations of valleys in
this measure, and are then adjusted to coincide with known paragraph boundaries.
TextTiling is straightforward to implement, and does not require extensive train-
ing on labeled data. However, TextTiling is designed for a slightly different problem
than the one addressed in this study. Since it is designed to identify the subtopics
within a single text and not to find breaks between consecutive documents (?), a

comparison of TextTiling with the system we propose is difficult. Furthermore,
TextTiling segments at the paragraph level, while this work doesn’t assume the
presence of explicit paragraph boundaries. Applications such as video retrieval may
use speech recognition transcripts or closed captions that lack structural markup.
Since TextTiling is widely used and implemented, we examine its behavior on our
task in Section 9.
Another approach, introduced by Reynar (?), is a graphically motivated segmen-
tation technique called dotplotting. This technique depends exclusively on word
repetition to find tight regions of topic similarity.
Instead of focusing on strict lexical repetition, Kozima (?) uses a semantic net-
work to track cohesiveness of a document in a lexical cohesion profile. This system
computes the lexical cohesiveness between two words by “activating” the node for
one word and observing the “activity value” at the other word after some number
of iterations of “spreading activation” between nodes. The network is trained auto-
matically using a language-specific knowledge source (a dictionary of definitions).
Kozima generalizes lexical cohesiveness to apply to a window of text, and plots the
cohesiveness of successive text windows in a document, identifying the valleys in
the measure as segment boundaries.

file:///ext4Data/UFSCar/papers/Segmentação/novos/ml.pdf



****************************************************************************
****************************************************************************





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


****************************************************************************
****************************************************************************



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

****************************************************************************
****************************************************************************




Rather than using the raw term frequency vector, some
approaches, e.g., BAYESSEG[2], [3], TopicTiling[4], TSM[5],
NTSeg[6], employ topic modeling to improve performance,
such as latent Dirichlet allocation[7]

file:///ext4Data/UFSCar/papers/atalhos/Domain-independent unsupervised text segmentation for data management.pdf


****************************************************************************
****************************************************************************

Fórmula do WindowDiff
Evaluation metric. We have measured the per-
formance of the segmenters with the WindowDiff
metric (Pevzner and Hearst, 2002). It is computed
by sliding a window through reference and through
segmentation output and, at each window position,
comparing the number of reference breaks to the
number of breaks inserted by the segmenter (hypo-
thetical breaks). It is a penalty measure which re-
ports the number of windows where the reference
and hypothetical breaks do not match, normalized
by the total number of windows. In Equation 21,
ref and hyp denote the number of reference and hy-
pothetical segment breaks within a window.

file:///media/ovidiojf/Data/UFSCar-Bk/UFSCar-2016/Qualificação/Pesquisa/Quali/sentence sliding window/Linear text segmentation using affinity propagation.pdf



****************************************************************************
****************************************************************************


The depth score (s) at each gap is then given by s=(yi-1′-yi′)+(yi+1′-yi′)
→ https://www.inf.ed.ac.uk/teaching/courses/anlp/lectures/27/



****************************************************************************
****************************************************************************











****************************************************************************
****************************************************************************









****************************************************************************
****************************************************************************






****************************************************************************
****************************************************************************






