/*
 * Copyright (c) 2019 AkashV22
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.dao;

import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.model.GameDataModel;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import static com.akashv22.app.simpleswaggerfirstjooqspringbootapp.generated.jooq.tables.Game.GAME;

@Repository("gameDao")
public class JooqGameDao implements GameDao {
    private final DSLContext create;

    public JooqGameDao(DSLContext dslContext) {
        this.create = dslContext;
    }

    @Override
    public GameDataModel findById(int id) {
        var record = create.select(GAME.ID, GAME.NAME, GAME.YEAR)
                .from(GAME)
                .where(GAME.DELETED.eq(false))
                .and(GAME.ID.eq(id))
                .fetchOne()
                ;

        return recordToDataModel(record);
    }

    @Override
    public List<GameDataModel> findAll() {
        var records = create.select(GAME.ID, GAME.NAME, GAME.YEAR)
                .from(GAME)
                .where(GAME.DELETED.eq(false))
                .fetch()
                ;

        return records
                .stream()
                .map(this::recordToDataModel)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public boolean exists(int id) {
        return create.fetchExists(
                create.select(GAME.ID)
                        .from(GAME)
                        .where(GAME.DELETED.eq(false))
                        .and(GAME.ID.eq(id))
        );
    }

    @Override
    public GameDataModel insert(GameDataModel game) {
        var record = create.insertInto(GAME)
                .columns(GAME.NAME, GAME.YEAR, GAME.DATEADDED)
                .values(game.name, game.year, now())
                .returningResult(GAME.ID, GAME.NAME, GAME.YEAR)
                .fetchOne()
                ;

        return recordToDataModel(record);
    }

    @Override
    public GameDataModel update(GameDataModel game) {
        int updated = create.update(GAME)
                .set(GAME.NAME, game.name)
                .set(GAME.YEAR, game.year)
                .set(GAME.DATEUPDATED, now())
                .where(GAME.DELETED.eq(false))
                .and(GAME.ID.eq(game.id))
                .execute()
                ;

        return updated == 1 ? findById(game.id) : null;
    }

    @Override
    public GameDataModel delete(int id) {
        var record = create.update(GAME)
                .set(GAME.DELETED, true)
                .set(GAME.DATEDELETED, now())
                .where(GAME.DELETED.eq(false))
                .and(GAME.ID.eq(id))
                .returningResult(GAME.ID, GAME.NAME, GAME.YEAR)
                .fetchOne()
                ;

        return recordToDataModel(record);
    }

    private GameDataModel recordToDataModel(Record record) {
        if(record == null) {
            return null;
        }
        return new GameDataModel(record.getValue(GAME.ID), record.getValue(GAME.NAME), record.getValue(GAME.YEAR));
    }

    private Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
