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

package com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.endpoint.impl;

import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.core.endpoint.exception.InvalidIdException;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.endpoint.GameApiImplementor;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.endpoint.exception.GameNotFoundException;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.model.Game;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.service.GameService;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.generated.swagger.model.GameApiModel;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Component("gameApiImplementor")
public class DefaultGameApiImplementor implements GameApiImplementor {
    private final GameService gameService;

    public DefaultGameApiImplementor(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public GameApiModel addOrUpdateGame(@Valid GameApiModel body) {
        Integer gameId = body.getId();
        if (gameId != null && gameId < 0) {
            throw new InvalidIdException(gameId);
        }

        return gameToApiModel(validate(gameId, gameService.saveGame(apiModelToGame(body))));
    }

    @Override
    public GameApiModel deleteGame(Integer gameId) {
        if (gameId <= 0) {
            throw new InvalidIdException(gameId);
        }

        return gameToApiModel(validate(gameId, gameService.deleteGame(gameId)));
    }

    @Override
    public GameApiModel getGame(Integer gameId) {
        if (gameId <= 0) {
            throw new InvalidIdException(gameId);
        }

        return gameToApiModel(validate(gameId, gameService.getGame(gameId)));
    }

    @Override
    public List<GameApiModel> getGames() {
        return gameService.getGames()
                .stream()
                .map(this::gameToApiModel)
                .collect(Collectors.toList())
                ;
    }

    private Game apiModelToGame(GameApiModel apiModel) {
        Integer id = apiModel.getId();
        return new Game(id == null ? 0 : id, apiModel.getName(), apiModel.getYear());
    }

    private Game validate(Integer gameId, Game game) {
        if (gameId != null && game == null) {
            throw new GameNotFoundException(gameId);
        }
        return game;
    }

    private GameApiModel gameToApiModel(Game game) {
        return new GameApiModel()
                .id(game.id)
                .name(game.name)
                .year(game.year)
                ;
    }
}
