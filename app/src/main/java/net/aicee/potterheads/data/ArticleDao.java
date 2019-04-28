/*
    Copyright 2019 Ifeanyichukwu Onyeabor

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package net.aicee.potterheads.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM Articles")
    List<Article> getAll();

    @Query("SELECT id, aspect_ratio, thumb, author, title, published_date FROM articles")
    List<Article> getAllExceptBody();

    @Query("SELECT * FROM ARTICLES where id LIKE  :id")
    Article findById(int id);

    @Query("SELECT COUNT(*) from ARTICLES")
    int countArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Article> articles);

    @Insert
    void insert(Article article);

    @Delete
    void delete(Article article);

    @Query("DELETE FROM articles")
    void deleteAll();

}
