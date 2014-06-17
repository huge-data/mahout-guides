/**
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

package com.ibm.taste.example.movie.servlet;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.Recommender;

/**
 * <p>A singleton which holds an instance of a {@link Recommender}. This is used to share
 * a {@link Recommender} between {@link RecommenderServlet} and <code>RecommenderService.jws</code>.</p>
 */
public final class MovieRecommenderSingleton {

  private final Recommender recommender;

  private static MovieRecommenderSingleton instance;

  public static synchronized MovieRecommenderSingleton getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Not initialized");
    }
    return instance;
  }

  public static synchronized void initializeIfNeeded(String recommenderClassName) throws TasteException {
    if (instance == null) {
      instance = new MovieRecommenderSingleton(recommenderClassName);
    }
  }

  private MovieRecommenderSingleton(String recommenderClassName) throws TasteException {
    if (recommenderClassName == null) {
      throw new IllegalArgumentException("Recommender class name is null");
    }
    try {
      recommender = Class.forName(recommenderClassName).asSubclass(Recommender.class).newInstance();
    } catch (ClassNotFoundException cnfe) {
      throw new TasteException(cnfe);
    } catch (InstantiationException ie) {
      throw new TasteException(ie);
    } catch (IllegalAccessException iae) {
      throw new TasteException(iae);
    }
  }

  public Recommender getRecommender() {
    return recommender;
  }

}
