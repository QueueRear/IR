package searchManager;

import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

import static java.lang.Math.log;

public class MySimilarity extends TFIDFSimilarity {
    @Override
    public float tf(float freq) {// 一个词汇/词组在单个文档中出现的次数，将来会与idf相乘得到tfidf weight
        float termWeight = 0;
        if (freq > 0) {
            termWeight = (float)(1 + log(freq) / log(10));
        }
        return termWeight;
    }

    @Override
    public float idf(long docFreq, long docCount) {
        return (float)(log(docCount / (docFreq + 1)) / log(10));
    }

    @Override
    public float lengthNorm(int numTerms) {
        return (float)(1.0 / Math.sqrt(log(numTerms)/log(10)));
    }

    @Override
    public float sloppyFreq(int i) {
        return 0;
    }

    @Override
    public float scorePayload(int i, int i1, int i2, BytesRef bytesRef) {
        return 0;
    }

}

