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

package com.akashv22.app.simpleswaggerfirstjooqspringbootapp.core.endpoint;

import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.game.endpoint.impl.GameEndpointImplementor;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.generated.swagger.api.GamesApi;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.generated.swagger.model.GameApiModel;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.openapispec.api.OpenApiSpecApi;
import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.openapispec.endpoint.OpenApiSpecEndpointImplementor;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/")
public class CoreApiEndpoint implements GamesApi, OpenApiSpecApi {
    private final GameEndpointImplementor gameEndpointImplementor;
    private final OpenApiSpecEndpointImplementor openApiSpecEndpointImplementor;

    public CoreApiEndpoint(
            GameEndpointImplementor gameEndpointImplementor
            , OpenApiSpecEndpointImplementor openApiSpecEndpointImplementor
    ) {
        this.gameEndpointImplementor = gameEndpointImplementor;
        this.openApiSpecEndpointImplementor = openApiSpecEndpointImplementor;
    }

    @Override
    public GameApiModel addOrUpdateGame(@Valid GameApiModel body) {
        return gameEndpointImplementor.addOrUpdateGame(body);
    }

    @Override
    public GameApiModel deleteGame(Integer gameId) {
        return gameEndpointImplementor.deleteGame(gameId);
    }

    @Override
    public GameApiModel getGame(Integer gameId) {
        return gameEndpointImplementor.getGame(gameId);
    }

    @Override
    public List<GameApiModel> getGames() {
        return gameEndpointImplementor.getGames();
    }

    @Override
    public Response getOpenApiSpecYaml() {
        return openApiSpecEndpointImplementor.getOpenApiSpecYaml();
    }
}
