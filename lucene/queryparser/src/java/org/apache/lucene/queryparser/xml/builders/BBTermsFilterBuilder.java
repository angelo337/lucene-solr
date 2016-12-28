package org.apache.lucene.queryparser.xml.builders;


import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.MatchAllDocsFilter;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.apache.lucene.queryparser.xml.BBTermBuilder;
import org.apache.lucene.queryparser.xml.ParserException;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Builder for {@link TermsFilter}
 */
public class BBTermsFilterBuilder implements FilterBuilder {

  private final BBTermBuilder termBuilder;

  public BBTermsFilterBuilder(BBTermBuilder termBuilder) {
    this.termBuilder = termBuilder;
  }

  private class TermsFilterProcessor implements BBTermBuilder.BBTermProcessor {
    public List<Term> terms = new ArrayList<Term>();
    public void process(Term t) {
      terms.add(t);
    }
    
    public Filter getFilter() {
      final int termsSize = terms.size();
      if (termsSize == 0) {
          return new MatchAllDocsFilter();
      } else if (termsSize == 1) {
          return new TermFilter(terms.get(0));
      } else {
          return new TermsFilter(terms);
      }
    }
  }

  /*
    * (non-Javadoc)
    *
    * @see org.apache.lucene.xmlparser.FilterBuilder#process(org.w3c.dom.Element)
    */
  @Override
  public Filter getFilter(final Element e) throws ParserException {

    TermsFilterProcessor tp = new TermsFilterProcessor();

    termBuilder.extractTerms(tp, e);

    return tp.getFilter();
  }
}
