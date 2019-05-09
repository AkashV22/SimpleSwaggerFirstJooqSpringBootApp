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

package com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.service.impl;

import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.dao.GameDao;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.model.GameDataModel;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.service.GameService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("gameService")
public class SpringGameService implements GameService {
    private final GameDao gameDao;

    public SpringGameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    @Transactional
    @Override
    public GameDataModel getGame(int id) {
        return gameDao.findById(id);
    }

    @Transactional
    @Override
    public List<GameDataModel> getGames() {
        return gameDao.findAll();
    }

    @Transactional
    @Override
    public GameDataModel saveGame(GameDataModel game) {
        int id = game.id;

        if (id == 0) {
            return gameDao.insert(game);
        }

        return gameDao.exists(id) ? gameDao.update(game) : null;
    }

    @Transactional
    @Override
    public GameDataModel deleteGame(int id) {
        return gameDao.delete(id);
    }
}
