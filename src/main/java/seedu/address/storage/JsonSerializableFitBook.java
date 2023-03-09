package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.FitBook;
import seedu.address.model.ReadOnlyFitBook;
import seedu.address.model.client.Client;

/**
 * An Immutable FitBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableFitBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Clients list contains duplicate client(s).";

    private final List<JsonAdaptedClient> clients = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableFitBook} with the given clients.
     */
    @JsonCreator
    public JsonSerializableFitBook(@JsonProperty("clients") List<JsonAdaptedClient> clients) {
        this.clients.addAll(clients);
    }

    /**
     * Converts a given {@code ReadOnlyFitBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableFitBook}.
     */
    public JsonSerializableFitBook(ReadOnlyFitBook source) {
        clients.addAll(source.getClientList().stream().map(JsonAdaptedClient::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code FitBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public FitBook toFitBookModelType() throws IllegalValueException {
        FitBook fitBook = new FitBook();
        for (JsonAdaptedClient jsonAdaptedClient : clients) {
            Client client = jsonAdaptedClient.toFitBookModelType();
            if (fitBook.hasClient(client)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            fitBook.addClient(client);
        }
        return fitBook;
    }

}
