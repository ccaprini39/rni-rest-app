package com.rnirest.rnirestapp.namesearch.config;

import com.basistech.rni.es.DocScoreFunctionBuilder;
import com.basistech.rni.es.RNIFunctionScoreQueryBuilder.FilterFunctionBuilder;
import com.basistech.rni.match.Name;
import com.basistech.rni.match.NameBuilder;
import com.basistech.rni.match.date.DateSpec;
import com.basistech.rni.match.date.DateSpecBuilder;
import com.basistech.util.NEConstants;
import com.basistech.util.Pathnames;
import com.rnirest.rnirestapp.namesearch.model.AdvancedNamesearchRequest;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.rescore.QueryRescoreMode;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTransform {
    private static final Logger logger = LoggerFactory.getLogger(DataTransform.class);
    private static String RNI_ROOT = "C:/Users/Collin/Desktop/elasticsearch/elasticsearch-7.13.2/plugins/rni/bt_root";

    public static QueryRescorerBuilder createRescorer( AdvancedNamesearchRequest ansr ){
        String name = ansr.getName();
        String dob = ansr.getDob();
        Integer window = ansr.getWindow();
        Integer nameWeight = ansr.getNameWeight();
        Integer dobWeight = ansr.getDobWeight();
        Double nameWeightDouble = Double.valueOf(nameWeight);
        Double dobWeightDouble = Double.valueOf(dobWeight);

        Pathnames.setBTRootDirectory(RNI_ROOT);
        Name n = NameBuilder.data(name).entityType(NEConstants.NE_TYPE_PERSON).build();
        DateSpec d = DateSpecBuilder.dateFromString(dob);

        DocScoreFunctionBuilder docScoreBuilder1 = new DocScoreFunctionBuilder()
                .queryField("rni_name", name, nameWeightDouble)
                .queryField("rni_dob", dob, dobWeightDouble);

        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(
                    //DocScoreFunctionBuilder docScoreBuilder1 = new DocScoreFunctionBuilder()
                    docScoreBuilder1
        );

        QueryRescorerBuilder rescorer = new QueryRescorerBuilder(functionScoreQueryBuilder);
        rescorer.setScoreMode(QueryRescoreMode.Max);
        rescorer.setQueryWeight(0.0f);
        rescorer.windowSize(window);

        logger.info(rescorer.toString());

        return rescorer;
    }

    public static BoolQueryBuilder createQuery( AdvancedNamesearchRequest ansr ) {
        String name = ansr.getName();
        String dob = ansr.getDob();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder    
            .should(QueryBuilders.matchQuery("rni_name", name))
            .should(QueryBuilders.matchQuery("rni_dob", dob));

        return boolQueryBuilder;
    }

}
