import { BaseReactUiElement, ReactUiElementFactory } from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';

function MeasurementsChartComponent(props: { element: MeasurementsChart }) {
    for (const prop of props.element.state.keys()) {
        const [value, setValue] = useState(props.element.state.get(prop));
        props.element.state.set(prop, value);
        props.element.stateSetters.set(prop, setValue);
    }
    useEffect(() => {
        props.element.state.forEach((value, key) => {
            props.element.stateSetters.get(key)?.(value);
        });
    }, [props.element]);

    const modal = useModal();
    return (
        <div
            className="webpeer-container"
            key={props.element.id}
            style={{
                display: 'flex',
                flexDirection: 'column',
            }}
        >
            <div
                className="webpeer-container-header"
                key="header"
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                }}
            >
                <div style={{ flexGrow: 0 }} key="id">
                    {props.element.tag}
                </div>
                <div style={{ flexGrow: 1 }} key="glue" />
                <button
                    className="webpeer-details-button"
                    style={{ flexGrow: 0 }}
                    onClick={() => {
                        modal.open();
                    }}
                >
                    Details
                </button>
            </div>
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
        </div>
    );
}

export class MeasurementsChart extends BaseReactUiElement {
    constructor(model: any) {
        super(
            [],
            ['month', 'monthInt', 'nextMonthEnabled', 'previousMonthEnabled'],
            [],
            ['showNextMonth', 'showPreviousMonth'],
            model
        );
    }
    createReactElement(): React.ReactElement {
        return React.createElement(MeasurementsChartComponent, {
            element: this,
            key: this.id,
        });
    }
}

export class TestMeasurementsChartFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new MeasurementsChart(model);
    }
}
