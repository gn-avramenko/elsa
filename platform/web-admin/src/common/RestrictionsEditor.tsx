import { initStateSetters } from 'admin/src/common/component';
import { RestrictionsEditorSkeleton } from 'admin/src-gen/common/RestrictionsEditorSkeleton';
import { theme } from 'antd';
import {
    DownOutlined,
    MinusOutlined,
    PlusOutlined,
    UpOutlined,
} from '@ant-design/icons';
import { BaseUiElement } from 'webpeer-core';
import { RestrictionEditorComponent } from 'admin/src/common/RestrictionEditor';
import { ReactElement } from 'react';

function RestrictionsEditorFC(props: { element: RestrictionsEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    const calculateMaxColumnsCount = (
        startingValue: number,
        nestedCount: number,
        children: BaseUiElement[]
    ) => {
        let value = startingValue;
        children.forEach((it) => {
            const ed = it as RestrictionEditorComponent;
            if (ed.getRestrictionType() === 'SIMPLE') {
                if (value < nestedCount + 3) {
                    value = nestedCount + 3;
                }
                return;
            }
            var newCount = calculateMaxColumnsCount(
                value,
                nestedCount + 1,
                ed.children!
            );
            if (newCount > value) {
                value = newCount;
            }
        });
        return value;
    };
    const maxColumnCount = calculateMaxColumnsCount(3, 0, props.element.children!);

    const rows = [] as (() => ReactElement)[][];
    const collectElements = (elements: BaseUiElement[], nestedCount: number) => {
        elements.forEach((it) => {
            const ch = it as RestrictionEditorComponent;
            const row = [] as (() => ReactElement)[];
            if (ch.getRestrictionType() === 'SIMPLE') {
                row.push(() => (
                    <div
                        key={`property-${ch.id}`}
                        style={{
                            gridRow: rows.length + 1,
                            gridColumn: `1/${nestedCount + 1}`,
                        }}
                    >
                        {ch.findByTag('property')?.createReactElement()}
                    </div>
                ));
                row.push(() => (
                    <div
                        key={`condition-${ch.id}`}
                        style={{
                            gridRow: rows.length + 1,
                            gridColumn: nestedCount + 2,
                        }}
                    >
                        {ch.findByTag('condition')?.createReactElement()}
                    </div>
                ));
                row.push(() => (
                    <div
                        key={`value-${ch.id}`}
                        style={{
                            gridRow: rows.length + 1,
                            gridColumn: nestedCount + 3,
                        }}
                    >
                        {ch.findByTag('value')?.createReactElement()}
                    </div>
                ));
                row.push(() => (
                    <div
                        key={`tools-${ch.id}`}
                        style={{
                            width: 35,
                            display: 'flex',
                            flexDirection: 'row',
                            alignItems: 'center',
                            gridRow: rows.length + 1,
                            gridColumn: maxColumnCount + 1,
                        }}
                    >
                        <PlusOutlined
                            style={{
                                paddingLeft: token.paddingXS,
                                color: props.element.isReadonly()
                                    ? token.colorTextDisabled
                                    : undefined,
                            }}
                            onClick={() => {
                                // if (!props.element.isReadonly()) {
                                //     props.element.sendAdd({
                                //         idx,
                                //     });
                                // }
                            }}
                        />
                        <MinusOutlined
                            style={{
                                paddingLeft: token.paddingXS,
                                color: props.element.isReadonly()
                                    ? token.colorTextDisabled
                                    : undefined,
                            }}
                            onClick={() => {
                                // if (!props.element.isReadonly()) {
                                //     props.element.sendDelete({
                                //         idx,
                                //     });
                                // }
                            }}
                        />
                        <UpOutlined
                            style={{
                                paddingLeft: token.paddingXS,
                                color: props.element.isReadonly()
                                    ? token.colorTextDisabled
                                    : undefined,
                            }}
                            onClick={() => {
                                if (!props.element.isReadonly()) {
                                    // props.element.sendMoveUp({
                                    //     idx,
                                    // });
                                }
                            }}
                        />
                        <DownOutlined
                            style={{
                                paddingLeft: token.paddingXS,
                                color: props.element.isReadonly()
                                    ? token.colorTextDisabled
                                    : undefined,
                            }}
                            onClick={() => {
                                // if (
                                //     !props.element.isReadonly() &&
                                //     idx !== arr.length - 1
                                // ) {
                                //     props.element.sendMoveDown({
                                //         idx,
                                //     });
                                // }
                            }}
                        />
                    </div>
                ));
            } else {
            }
            rows.push(row);
        });
    };
    collectElements(props.element.children!, 0);
    const elements = [] as (() => ReactElement)[];
    rows.forEach((row) => row.forEach((i) => elements.push(i)));
    return (
        <div
            style={{
                width: 'calc(100% - 60px)',
                display: 'grid',
                gridTemplateColumns: '180px 1fr 30px',
                columnGap: 10,
            }}
        >
            {elements.map((elm) => elm())}
        </div>
    );
}

export class RestrictionsEditorComponent extends RestrictionsEditorSkeleton {
    functionalComponent = RestrictionsEditorFC;
}
