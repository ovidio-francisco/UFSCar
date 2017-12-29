
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


Explicação e formula do TFIDF 

usando para pesar os termos da bag-of-words

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/Filtering Relevant Text Passages Based on Lexical Cohesion.pdf


file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers recentes/Automatic text summarization and it's methods - A review.pdf

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/Topic segmentation algorithms for text summarization and passage retrieval: An exhaustive evaluation.pdf

Fala de VSM também:
file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/Filtering Relevant Text Passages Based on Lexical Cohesion.pdf

****************************************************************************
****************************************************************************

Usa TT e C99 como baseline

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers recentes/Exogenous approach to improve topic segmentation.pdf


****************************************************************************
****************************************************************************

Fórmula do Pk em 
file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers recentes/Extractive summarization of multi-party meetings through discourse segmentation.pdf
file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers 2/Adapting_and_comparing_linear_segmentation_methods.pdf
Mostra uma imagen do C99

Fórumla do Pk e WinDiff e explica BEM:
file:///ext4Data/UFSCar/papers/Segmentação/novos/ml.pdf

Fórumla do Pk e WinDiff e explica BEM:
file:///ext4Data/UFSCar/papers/Segmentação/Novos Segmentadores/Minimum Cut Model for Spoken Lecture Segmentation.pdf


The following is the P μ assessmentmethod:
P μ (ref, hyp) =∑ D—μ(i, j)( δ ref (i, j) ⊕ δ hyp (i, j))
file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/Text segmentation based on PLSA-TextTiling model.pdf


****************************************************************************
****************************************************************************

Fala de várias abordagens de segmentação

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers recentes/Automatic text summarization and it's methods - A review.pdf

****************************************************************************
****************************************************************************

Fala do Teste de Friedman
Fala do 

file:///ext4Data/UFSCar/papers/Segmentação/more papers/Statistical Comparisons of Classifiers over Multiple Data Sets.pdf

****************************************************************************
****************************************************************************

"Based on the observation of Halliday and Hasan
(1976) ..."

Halliday, M.A.K; and Ruqayia Hasan (1976): Cohesion in English. London: Longman.

file:///ext4Data/UFSCar/papers/Segmentação/Novos Segmentadores/How Text Segmentation Algorithms Gain from Topic Models.pdf

"M A K Halliday and Ruqaiya Hasan. 1976. Cohesion in
English, volume 1 of English Language Series. Long-
man."

****************************************************************************
****************************************************************************

Explica e dá formula do depth score

file:///ext4Data/UFSCar/papers/Segmentação/Novos Segmentadores/Text Segmentation with Topic Models.pdf



****************************************************************************
****************************************************************************


"For TextTiling the best
performance was achieved with μ − 0 . 8 ⋅ σ , and
k = 3 (window size)."
file:///ext4Data/UFSCar/papers/Segmentação/more papers/Outros - Arabic/IRI07_Comparative_Analysis.pdf

****************************************************************************
****************************************************************************

Explica o threshold do TT

Hearst’s TextTiling algorithm determines the strength of the decrease (depth score) of the
cosine similarity scores by summing up the distance from the peaks around the current
boundary candidate. The candidate is selected as a boundary if there is no higher decrease
that exceeds a threshold value Θ within a window equal to the minimum segment size M .
The threshold is given by the difference of the mean μ and the standard deviation σ of their
depth scores. A more conservative measure resulting in a higher precision but lower recall
can be chosen by setting the threshold Θ = μ − σ/2 (cf. [Hea97]).

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/Filtering Relevant Text Passages Based on Lexical Cohesion.pdf

****************************************************************************
****************************************************************************

Um gráfico que mostra 3 medidas (Pk, Pk' e WinDiff) para 3 agls (DTTS, C99 e TT)

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/Text Mining for Meeting Transcript Analysis to Extract Key Decision Elements.pdf

****************************************************************************
****************************************************************************

Uma implementação de C99 e TT por Choi em

https://code.google.com/archive/p/uima-text-segmenter/
https://code.google.com/archive/p/uima-text-segmenter/source/default/source
https://code.google.com/archive/p/uima-text-segmenter/downloads

file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers/How text segmentation algorithms gain from topic models.pdf

****************************************************************************
****************************************************************************

Fala de abordagens baseadas em Tópicos e baseadas em Grafos:
file:///ext4Data/UFSCar/papers/Segmentação/more papers/papers recentes/Extractive summarization of multi-party meetings through discourse segmentation.pdf


****************************************************************************
****************************************************************************

The main idea in the 
Texttiling algorithm is that the vocabulary does not revolve dra-
matically during the course of one topic segment, however it
undergoes a significant change wherever the topic is shifted. In
this approach two windows are rolled over the elements in the
input word sequence and compared according to the patterns of
lexical distribution.


paper: de onde ????



****************************************************************************
****************************************************************************

Na página 17 mostra uma imagem e explica o C99

file:///ext4Data/UFSCar/papers/Segmentação/Novos Segmentadores/Minimum Cut Model for Spoken Lecture Segmentation.pdf


****************************************************************************
****************************************************************************

Introdução legal:

"Natural language understanding is arguably one of the most compelling scientific fron-
tiers, only now beginning to be probed through advances in statistical natural language
processing, machine learning, linguistics, and cognitive science. In this thesis, we address
one of the structural pieces in the required scaffolding, the problem of text segmentation."
...
"In this case, the definition of a topic boundary is clear-cut,
because it corresponds to a document boundary. There are real-world problems where this
scenario is relevant. For example, research work has been conducted on broadcast news
segmentation, where the goal is to partition the broadcast news transcripts into a set of
distinct news segments"

file:///ext4Data/UFSCar/papers/Segmentação/Novos Segmentadores/Minimum Cut Model for Spoken Lecture Segmentation.pdf

"
2.1.1 Lexical Cohesion Theory
One common assumption that threads its way into the design of many segmentation al-
gorithms is the notion that lexical repetition indicates topic continuity, while changes in
lexical distribution signal topic changes.
This principle was first formalized in the linguistic work of Halliday and Hasan (1976) on
Cohesion Theory. The theory postulates that discourse is constrained by certain grammati-
cal and lexical cohesion requirements. At the semantic and syntactic level these constraints
include devices of reference, substitution, ellipsis, and conjunction. At the lexical level, the
narratives are tied together by way lexical cohesion or word repetition."



****************************************************************************
****************************************************************************

imagen de segmentação de matrix similar a do C99:

http://iopscience.iop.org/article/10.1088/1367-2630/16/11/115014#njp503749f1
http://iopscience.iop.org/article/10.1088/1367-2630/16/11/115014


****************************************************************************
****************************************************************************

"In a final step, a top-down hierarchical clustering algorithm is  performed to split the document into m segments. 
This algorithm starts with the whole document considered as one segment and splits off segments until the stop criteria 
are met, e.g. the number of segments or a similarity threshold. 
At this, the ranking matrix is split at indices i, j that maximize the inside density function D"


https://www.scribd.com/document/320071178/Comparative-Analysis-of-c99-and-Topictiling-Text-Segmentation-Algorithms

****************************************************************************
****************************************************************************


"C99 then finds topic boundaries by recursively seeking the optimum density of
matrices along the rank matrix diagonal. The algorithm stops when the optimal boundaries
returned are the end of the current matrix or, if the user gave this parameter to the
algorithm, when the maximum number of text segments is reached."

http://www.scitepress.org/Documents/2008/17282/17282.pdf










****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************



****************************************************************************
****************************************************************************





